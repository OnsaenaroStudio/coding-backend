package io.github.onsaenaro.endpoint.code.controller

import io.github.onsaenaro.data.ResponseForm
import io.github.onsaenaro.endpoint.code.entity.CodeRequest
import io.github.onsaenaro.endpoint.code.entity.CodeResult
import io.github.onsaenaro.endpoint.code.entity.Language
import io.github.onsaenaro.endpoint.code.entity.Result
import io.github.onsaenaro.util.responseGenerator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/code")
class CodeController {

    @PostMapping
    fun execute(
        @RequestBody(required = true) req: CodeRequest
    ): ResponseEntity<ResponseForm<CodeResult>> {
        val workDir = File("/tmp/onsaenaro/code/${UUID.randomUUID()}")
        workDir.mkdirs()

        try {
            val file = File(workDir, "Main.${req.language.exp}").also {
                it.writeText(req.code)
            }

            val compile = ProcessBuilder(
                "docker", "run", "--rm",
                "--memory", "256m",
                "--cpus", "0.5",
                "--network", "none",
                "-v", "${workDir.absolutePath}:/code",
                req.language.image,
                "sh", "-c", "cd /code && ${buildScriptGenerator(req.language)}"
            ).redirectErrorStream(true).start()

            if (!compile.waitFor(10, TimeUnit.SECONDS)) {
                compile.destroyForcibly()
                return responseGenerator(408, null, "Compilation timed out")
            }

            val compileOutput = compile.inputStream.bufferedReader().readText()
            if (compile.exitValue() != 0) return responseGenerator(400, null, compileOutput)

            val start = System.currentTimeMillis()
            val run = ProcessBuilder(
                "docker", "run", "--rm",
                "--memory", "256m",
                "--cpus", "0.5",
                "--network", "none",
                "-v", "${workDir.absolutePath}:/code",
                req.language.image,
                "sh", "-c", runScriptGenerator(req.language)
            ).redirectErrorStream(false).start()

            run.outputStream.bufferedWriter().use { it.write(req.input) }

            if (!run.waitFor(10, TimeUnit.SECONDS)) {
                run.destroyForcibly()
                return responseGenerator(408, null, "Execution timed out")
            }

            val elapsed = System.currentTimeMillis() - start
            val stdout = run.inputStream.bufferedReader().readText()
            val stderr = run.errorStream.bufferedReader().readText()
            val status = if (run.exitValue() == 0) "success" else "runtime_error"

            return responseGenerator(200, CodeResult(
                Result.valueOf(status.uppercase()),
                stdout, stderr, elapsed),
                null
            )

        } catch (e: Exception) {
            e.printStackTrace()
            return responseGenerator(500, null, e.message)
        } finally {
            workDir.deleteRecursively()
        }
    }

    private fun buildScriptGenerator(lang: Language): String {
        return when(lang) {
            Language.KOTLIN -> "kotlinc Main.kt -include-runtime -d Main.jar 2>&1"
            Language.PYTHON -> "python3 Main.py"
            Language.JAVA -> "javac Main.java 2>&1"
            Language.CPP -> "g++ -o Main Main.cpp 2>&1"
        }
    }

    private fun runScriptGenerator(lang: Language): String {
        return when(lang) {
            Language.KOTLIN, Language.JAVA -> "java -jar /code/Main.jar"
            Language.PYTHON -> "python3 Main.py"
            Language.CPP -> "./Main"
        }
    }
}