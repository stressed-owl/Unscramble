import DataSource.wordsSet
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

/**
 * [totalScore] defines a user's total score at the end of the game
 */
var totalScore = 0

/**
 * [usedWords] set is used to store already used words in a game and to prevent the repetitions
 */

var usedWords = mutableSetOf<String>()

/**
 * [main] is a starting point of the app
 */

fun main() = runBlocking {
    introduceToPlayer()
    guessWord()
}

/**
 * [introduceToPlayer] function is just basically prints some greetings to a player
 */

fun introduceToPlayer() {
    println("Welcome to \"Unscramble\". In this game, you have to guess the unscrambled word.")
    println("If you do not guess it, you own me your soul...\n(just kidding)")
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
 * [randomWord] function is just returns a new word that need to be guessed
 */
fun randomWord(): String = wordsSet.random()

/**
 * At the end of the game, [randomCompliment] function displays a message for a player with the earned total score
 */

fun randomCompliment(totalScore: Int): String {
    val maxEarnedPoints = POINTS_PER_WORD * wordsSet.size
    return when (totalScore) {
        0 -> "Very bad, I'm disappointed!!!!"
        in 50..150 -> "Not bad!"
        in 151..200 -> "Good job!"
        in 201..250 -> "Awesome!"
        in 251..300 -> "Fucking monster. I'm as proud as Punch of you. Keep going"
        in 301..500 -> "Stop...Please, stop... You are too good for this game. I should beat my developers' asses to make this game way much harder"
        in 501..maxEarnedPoints -> "You guessed all of the words. Cool! I'm astonished and feel proud of you!\n(And yeah, that's everything that I wanted to say because I'm speechless)"
        else -> "Something went wrong"
    }
}

/**
 * [displayAfterGameMessage] function display the result message for a player after the game ends
 * @see [randomCompliment]
 */

fun displayAfterGameMessage(totalScore: Int) {
    println("You earned $totalScore. ${randomCompliment(totalScore = totalScore)}")
    println("User's list of guessed words: ")
    usedWords.forEach { word ->
        println("----> $word")
    }
}

/**
 * [guessWord] function holds the game logic
 */

suspend fun guessWord(wordToGuess: String = randomWord()): Unit = coroutineScope {
    fun isContinuePlaying(): Boolean {
        println("Do you want to continue playing? ('true' or 'false')")
        return readln().toBoolean()
    }
    while (true) {
        if (usedWords.contains(wordToGuess)) {
            guessWord(wordToGuess = randomWord())
        } else {
            usedWords.add(wordToGuess)
            delay(340L)
            displayWordToGuess(word = unscrambleWord(word = wordToGuess))
            val userGuess = readln()
            if (userGuess.equals(wordToGuess, ignoreCase = true)) {
                totalScore += POINTS_PER_WORD
                println("Cool, $POINTS_PER_WORD points were earned")
            } else {
                println("Nope. Try again")
                continue
            }
        }

        if (usedWords.size == wordsSet.size) {
            displayAfterGameMessage(totalScore = totalScore)
            exitProcess(0)
        }

        val answer = isContinuePlaying()
        if (answer) guessWord()
        else {
            displayAfterGameMessage(totalScore = totalScore)
            exitProcess(0)
        }
    }
}
