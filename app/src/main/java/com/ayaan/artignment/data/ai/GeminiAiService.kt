package com.ayaan.artignment.data.ai

import android.util.Log
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
            
            Please create 10-15 lines of educational content organized in 2-3 paragraphs with the following structure:
            - Introduction paragraph explaining the topic
            - 1 main concept paragraphs with bullet points and detailed explanations
            - Conclusion paragraph with practical tips and next steps in points
            
            Make the content educational, engaging, and suitable for beginners to intermediate learners.
            Include specific examples and practical applications where relevant.
            Give simple texts, avoid any markdown or HTML formatting, and do not use any code blocks.
            Format the response as plain text with clear paragraph breaks and bullet points.
            Do not add any personal opinions or subjective statements.
            Ensure the content is original and not copied from any source.
            The content should be informative, concise, and easy to understand.
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            Log.d("GeminiAiService", "AI response: ${response.text}")
            response.text ?: getDefaultLessonNotes(mentorName, lessonTitle)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("GeminiAiService", "Error generating lesson notes: ${e.message}")
            // Fallback to default notes if API fails
            getDefaultLessonNotes(mentorName, lessonTitle)
        }
    }

private fun getDefaultLessonNotes(mentorName: String, lessonTitle: String): String {
    return """
        Welcome to this engaging music lesson on $lessonTitle with $mentorName!

        In this session, we will dive into the essential concepts and practical skills needed to understand and enjoy music. 
        $mentorName will guide you through each topic, sharing expertise and passion for music education.

        Lesson Structure:
        • Introduction to the main theme and its relevance in music
        • Key musical concepts explained with clear examples
        • Step-by-step breakdowns of techniques or theory
        • Practical exercises and listening activities
        • Tips for practice and further exploration

        Throughout the lesson, you will:
        • Learn foundational music theory and terminology
        • Explore real-world applications, such as composing or performing
        • Practice with hands-on activities to reinforce your understanding
        • Discover how to listen actively and analyze musical pieces

        Best Practices for Music Learning:
        • Listen to a variety of music styles and genres
        • Practice regularly, even in short sessions
        • Record yourself to track progress and identify areas for improvement
        • Collaborate with others or join a music community
        • Stay curious and open to new musical experiences

        Remember, learning music is a journey that combines creativity, discipline, and enjoyment. $mentorName is here to support you every step of the way. Take your time, have fun, and let the music inspire you!

        Ready to start? Let’s make some music together! 🎶
    """.trimIndent()
}
}
