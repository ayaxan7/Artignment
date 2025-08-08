package com.ayaan.artignment.data.ai

import com.ayaan.artignment.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiAiService @Inject constructor() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 0.7f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 1000
        }
    )

    suspend fun generateLessonNotes(mentorName: String, lessonTitle: String): String {
        val prompt = """
            Generate comprehensive lesson notes for a tutorial lesson titled "$lessonTitle" taught by $mentorName.
            
            Please create 20-30 lines of educational content organized in 3-5 paragraphs with the following structure:
            - Introduction paragraph explaining the topic
            - 2-3 main concept paragraphs with bullet points and detailed explanations
            - Conclusion paragraph with practical tips and next steps
            
            Make the content educational, engaging, and suitable for beginners to intermediate learners.
            Include specific examples and practical applications where relevant.
            
            Format the response as plain text with clear paragraph breaks and bullet points.
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            response.text ?: getDefaultLessonNotes(mentorName, lessonTitle)
        } catch (e: Exception) {
            // Fallback to default notes if API fails
            getDefaultLessonNotes(mentorName, lessonTitle)
        }
    }

    private fun getDefaultLessonNotes(mentorName: String, lessonTitle: String): String {
        return """
            Welcome to this comprehensive lesson on $lessonTitle with $mentorName!

            In this lesson, we'll explore the fundamental concepts and practical applications of this topic. 
            $mentorName brings years of expertise to guide you through each step of the learning process.

            Key Learning Objectives:
            • Understand the core principles and foundations
            • Learn practical implementation techniques
            • Explore real-world applications and use cases
            • Develop hands-on skills through examples
            • Master best practices and common pitfalls to avoid

            Throughout this lesson, you'll discover how to apply these concepts effectively in your own projects. 
            $mentorName will share insights from industry experience and provide practical tips that you can 
            implement immediately.

            The content is structured to take you from basic understanding to practical mastery. Each section 
            builds upon the previous one, ensuring a smooth learning progression. You'll find examples, 
            exercises, and real-world scenarios that reinforce the concepts.

            Best Practices and Tips:
            • Take notes and practice alongside the lesson
            • Experiment with the concepts in your own environment
            • Don't hesitate to revisit sections for better understanding
            • Apply the learning to personal or professional projects
            • Connect with the community for additional support

            Remember, learning is a journey, and this lesson with $mentorName is designed to provide you with 
            both theoretical knowledge and practical skills. Take your time to absorb the information and 
            practice regularly.

            Ready to dive in? Let's begin this exciting learning adventure! 🚀
        """.trimIndent()
    }
}
