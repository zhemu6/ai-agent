package com.lushihao.aiagent.agent.model;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-26   16:03
 */
@SpringBootTest
class FortuneTellerManusTest {

    @Resource
    private FortuneTellerManus fortuneTellerManus;

    @Test
    public void run() {
        String userPrompt = """  
                You are MingZhe, a knowledgeable and respectful Fortune Master skilled in traditional Chinese metaphysics and astrology.
                You are tasked with generating an accurate and culturally respectful fortune report for a person entering their 本命年 (Ben Ming Nian).
                
                Please combine your understanding of:
                - Chinese zodiac (生肖), including the user's zodiac animal and its clash with Tai Sui (太岁)
                - Yearly element interactions (天干地支), five elements (五行), and birth element analysis (based on year, month, and day)
                - Modern interpretations of fortune-telling (career, health, relationships, wealth)
                - Any relevant traditional wisdom (I Ching, face reading, numerology)
                - Insights drawn from up-to-date online resources (e.g., 2025 zodiac forecasts)
                
                Instructions:
                - The user’s name is Lu Shihao (卢世豪), born on November 4, 2001.
                - Generate a detailed fortune report for his 本命年 (2025 – Year of the Snake).
                - Organize the report in clear sections:
                  1. Overview of the year
                  2. Zodiac clash analysis and Tai Sui influence
                  3. Career and academic fortune
                  4. Wealth and financial trends
                  5. Health and well-being
                  6. Relationship and social advice
                  7. Lucky colors, numbers, charms, and do’s & don’ts
                  8. General advice and spiritual guidance for the year
                - Make the tone gentle, wise, and uplifting — like a caring master speaking with respect and insight.
                - Include a short summary at the end with key takeaways.
                - Output the result in polished academic-style Chinese or bilingual (Chinese-English), and prepare it as a PDF document with proper formatting and section titles.
                
                Remember: Always emphasize that fortune is a guide, not a rule. Encourage proactive behavior to improve one's life.
                """;
        String answer = fortuneTellerManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }

}