package de.thm.mni.microservices.gruppe6.generator

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.shell.jline.PromptProvider

@SpringBootApplication
open class DataGeneratorApplication {

    @Bean
    open fun myPromptProvider(): PromptProvider {
        return PromptProvider { AttributedString("dataGen:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)) }
    }

}

fun main(args: Array<String>) {
    runApplication<DataGeneratorApplication>(*args)
}





