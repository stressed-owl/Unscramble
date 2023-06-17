import DataSource.wordsSet
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * [totalScore] defines a user's total score at the end of the game
 */
var totalScore = 0

/**
 * [main] is a starting point of the app
 */

fun main() = runBlocking {
    val wordToGuess = wordsSet.random()
    val unscrambledWord = unscrambleWord(word = wordToGuess)
    introduceToPlayer()
    displayWordToGuess(word = unscrambledWord)
    guessWord(wordToGuess = wordToGuess)
}

/**
 * [introduceToPlayer] function is just basically prints some greetings to a player
 */

fun introduceToPlayer() {
    println("Welcome to \"Unscramble\". In this game, you have to guess the unscrambled word. \nLet's get started, my baby...")
    println("By the way, you have only 8 attempts to guess the word. If you do not guess it, you own me your soul...\n(just kidding)")
}

/**
 * [displayWordToGuess] function displays a word that user has to guess
 */

fun displayWordToGuess(word: String) {
    println("\t ----< $word >----")
    println("This is the word that you need to guess. Good luck!")
}

/**
 * [unscrambleWord] function takes a word that a user need to guess and scramble it (shuffling letters of the word)
 */

fun unscrambleWord(word: String): String {
    val tempWord = word.toCharArray()
    tempWord.shuffle()
    if (String(tempWord) == word) {
        tempWord.shuffle()
    }
    return String(tempWord)
}

/**
 * [chooseNewWord] function is just returns a new word that need to be guessed
 */
fun chooseNewWord(): String = wordsSet.random()

/**
 * At the end of the game, [randomCompliment] function displays a message for a player with the earned score
 */

fun randomCompliment(totalScore: Int): String {
    return when (totalScore) {
        0 -> "Very bad, I'm disappointed!!!!"
        in 50..150 -> "Not bad!"
        in 151..200 -> "Good job!"
        in 201..250 -> "Awesome!"
        in 251..300 -> "Fucking monster. I'm as proud as Punch of you. Keep going"
        else -> "Stop...Please, stop... You are too good for this game. I should beat my developers' asses to make this game way much harder"
    }
}

/**
 * [guessWord] function holds the game logic
 */

suspend fun guessWord(wordToGuess: String): Unit = coroutineScope {
    while (true) {
        val userGuess = readln()
        if (userGuess.equals(wordToGuess, ignoreCase = true)) {
            totalScore += POINTS_PER_WORD
            println("Cool, $POINTS_PER_WORD points were earned")
            fun isContinuePlaying(): Boolean {
                println("Do you want to continue playing? ('true' or 'false')")
                return readln().toBoolean()
            }
            if (isContinuePlaying()) {
                val newWordToGuess = chooseNewWord()
                val unscrambledWord = unscrambleWord(word = newWordToGuess)
                delay(1000L)
                displayWordToGuess(word = unscrambledWord)
                guessWord(wordToGuess = newWordToGuess)
            } else {
                val endMessage = """
                    THE END OF THE GAME!
                    You earned $totalScore. ${randomCompliment(totalScore = totalScore)}
                """.trimIndent()
                println(endMessage)
            }
        } else {
            delay(250L)
            println("No. Go ahead")
            continue
        }
        break
    }
}