package model

/**
 * [POINTS_PER_WORD] constant holds max number of points that user can earn if they guess a word
 */
const val POINTS_PER_WORD = 50

/**
 * [DataSource] object holds a set of words for game
 */
object DataSource {
    val wordsList = listOf(
        Word(
            original = "umbrella",
            definition = "a device for protection against the rain, consisting of a stick with a folding frame covered in material at one end and usually a handle at the other, or a similar, often larger, device used for protection against the sun",
            difficultyLevel = "Easy",
        ),
        Word(
            original = "ambiguous",
            definition = "having or expressing more than one possible meaning, sometimes intentionally",
            difficultyLevel = "Medium",
        ),
        Word(
            original = "repel",
            definition = "to force someone or something to stop moving towards you or attacking you",
            difficultyLevel = "Medium",
        ),
        Word(
            original = "thwart",
            definition = "to stop something from happening or someone from doing something",
            difficultyLevel = "Medium",
        ),
        Word(
            original = "wage",
            definition = "a particular amount of money that is paid, usually every week, to an employee, especially one who does work that needs physical skills or strength, rather than a job needing a college education",
            difficultyLevel = "Easy",
        ),
        Word(
            original = "black hat hacker",
            definition = "a hacker (= a person who gets into computer systems without permission) who does this for criminal or bad reasons",
            difficultyLevel = "Hard",
        ),
        Word(
            original = "floodwaters",
            definition = "water left by flooding",
            difficultyLevel = "Medium",
        ),
        Word(
            original = "white hat hacker",
            definition = "a hacker (= a person who gets into computer systems without permission) who has morally good reasons for doing this",
            difficultyLevel = "Hard",
        ),
        Word(
            original = "denial-of-service attack",
            definition = "an occasion when a computer network or website is intentionally prevented from working correctly, by a person or group sending a large amount of data at one time",
            difficultyLevel = "Hard",
        ),
    )
}