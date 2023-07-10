import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import model.DataSource.wordsList
import model.DifficultyLevel
import model.POINTS_PER_WORD
import model.Word
import kotlin.system.exitProcess

/**
 * [totalScore] defines a user's total score at the end of the game
 */
var totalScore = 0

/**
 * [wordsNumber] holds the number of words
 */

var wordsNumber = wordsList.size

/**
 * [usedWords] set is used to store already used words in a game and to prevent the repetitions
 */

var usedWords = mutableSetOf<Word>()

/**
 * [customSetOfWords] set is used to store user's custom words
 */

var customSetOfWords = mutableSetOf<Word>()

/**
 * [rankedMapOfWords] map holds the level of difficulty and corresponding list of words
 * @see groupBy
 */

var rankedMapOfWords = wordsList.groupBy { it.difficultyLevel }

/**
 * [main] is a starting point of the app
 */

fun main() = runBlocking {
    introduceToPlayer()
    guessingWordLogic()
}

/**
 * [introduceToPlayer] function is just basically prints some greetings to a player
 */

fun introduceToPlayer() {
    println(
        "\"Unscramble\" is a captivating word puzzle game that challenges your ability to unravel jumbled letters and form meaningful words. \n" +
                "Put your vocabulary and problem-solving skills to the test as you race against the clock to unscramble as many words as possible. " +
                "\nAre you up for the challenge? Let the unscrambling begin!"
    )
}

/**
 * [displayWordToGuess] function displays a word that user has to guess
 */

fun displayWordToGuess(word: String) {
    println("\t/------> $word <------\\")
    println("This is the word that you need to guess. Good luck!")
    print("\n\t\t|------------|\t\t|------------|\n\t\t|\t skip\t |\t\t|\tcancel\t |\n\t\t|------------|\t\t|------------|\n")
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
 * At the end of the game, [randomCompliment] function displays a message for a player with the earned total score
 */

fun randomCompliment(totalScore: Int): String {
    val maxEarnedPoints = POINTS_PER_WORD.times(wordsNumber)
    return when (totalScore) {
        in 0..150 -> "Very bad, I'm disappointed!!!!"
        in 150..300 -> "Not bad!"
        in 300..750 -> "Good job!"
        in 750..1050 -> "Awesome!"
        in 1050..1450 -> "Fucking monster. I'm as proud as Punch of you. Keep going"
        in 1450..1600 -> "Stop...Please, stop... You are too good for this game. I should beat my developers' asses to make this game way much harder"
        in 1600..maxEarnedPoints -> "You guessed all of the words. Cool! I'm astonished and feel proud of you!\n(And yeah, that's everything that I wanted to say because I'm speechless)"
        else -> "Something went wrong"
    }
}

/**
 * [displayAfterGameMessage] function display the result message for a player after the game ends
 * @see [randomCompliment]
 */

fun displayAfterGameMessage(totalScore: Int) {
    println("\nTotally, you earned $totalScore. ${randomCompliment(totalScore = totalScore)}")
    println("List of the words that were used in this game: ")
    usedWords.map { word ->
        println("---> ${word.original} - ${word.definition}")
    }
    exitProcess(0)
}

/**
 * [guessingWordLogic] function holds the game logic
 */

suspend fun guessingWordLogic(wordsToGuess: Map<String, List<Word>> = rankedMapOfWords): Unit = coroutineScope {
    val wordsNeedToGuess: List<Word> = when (customizeAndChooseDifficulty()) {
        "predefined" -> {
            println("Enter level of difficulty")
            when (readln()) {
                DifficultyLevel.Easy.name -> {
                    POINTS_PER_WORD = 50
                    wordsToGuess[DifficultyLevel.Easy.name]!!
                }

                DifficultyLevel.Medium.name -> {
                    POINTS_PER_WORD = 75
                    wordsToGuess[DifficultyLevel.Medium.name]!!
                }

                else -> {
                    POINTS_PER_WORD = 100
                    wordsToGuess[DifficultyLevel.Hard.name]!!
                }
            }
        }
        "customized" -> {
            var isEnough = false
            while (!isEnough) {
                println("Enter the following information about a word:\n")
                print("Word original: ")
                val wordOriginal = readln()
                print("Difficulty level: ")
                val difficultyLevel = readln()
                print("Word definition: ")
                val wordDefinition = readln()
                customSetOfWords.add(
                    Word(
                        original = wordOriginal,
                        difficultyLevel = difficultyLevel.replaceFirstChar { it.uppercase() },
                        definition = wordDefinition,
                    )
                )
                println("Continue?")
                val answer = readln()
                if (answer == "y" || answer == "yes") { continue }
                else {
                    isEnough = true
                }
            }
            val customizedWords = customSetOfWords.groupBy { it.difficultyLevel }
            println("Enter level of difficulty and number of points you would like to earn per each word")
            val difficultyLevel = readln()
            val pointsPerWord = readln().toInt()
            POINTS_PER_WORD = pointsPerWord
            when (difficultyLevel) {
                DifficultyLevel.Easy.name -> {
                    customizedWords[DifficultyLevel.Easy.name] ?: emptyList()
                }

                DifficultyLevel.Medium.name -> {
                    customizedWords[DifficultyLevel.Medium.name] ?: emptyList()
                }

                else -> {
                    customizedWords[DifficultyLevel.Hard.name] ?: emptyList()
                }
            }
        }

        else -> {
            wordsToGuess[DifficultyLevel.Medium.name]!!
        }
    }

    while (true) {
        val wordNeedToGuess = wordsNeedToGuess.random()
        if (usedWords.contains(wordNeedToGuess)) {
            if (usedWords.size == wordsNeedToGuess.size) {
                displayAfterGameMessage(totalScore = totalScore)
            }
        } else {
            usedWords.add(wordNeedToGuess)
            delay(700L)
            displayWordToGuess(word = unscrambleWord(word = wordNeedToGuess.original))
            val userGuess = readln()
            if (userGuess == "skip") {
                continue
            }
            if (userGuess == "cancel") {
                break
            }
            if (userGuess.equals(wordNeedToGuess.original, ignoreCase = true)) {
                totalScore += POINTS_PER_WORD
                println("You earned $POINTS_PER_WORD points. Cool!")
                continue
            } else {
                println("Nope")
                continue
            }
        }
    }
}

/**
 * [customizeAndChooseDifficulty] function asks a user if they want to guess a predefined list of words or choose their own list of words
 */

fun customizeAndChooseDifficulty(): String {
    println("Would you like to play a customized game or predefined one?(type \"predefined\" or \"customized\")")
    return readln()
}
