import DataSource.wordsSet

fun main() {
    val wordToGuess = wordsSet.random()
    val unscrambledWord = unscrambleWord(word = wordToGuess)
    introduceToPlayer()
    displayWordToGuess(word = unscrambledWord)
    guessWord(wordToGuess = wordToGuess)
}

fun introduceToPlayer() {
    println("Welcome to \"Unscramble\". In this game, you have to guess the unscrambled word. \nLet's get started, my baby...")
    println("By the way, you have only 8 attempts to guess the word. If you do not guess it, you own me your soul...\n(just kidding)")
}

fun displayWordToGuess(word: String) {
    println("\t ----< $word >----")
    println("This is the word that you need to guess. Good luck!")
}

fun unscrambleWord(word: String): String {
    val tempWord = word.toCharArray()
    tempWord.shuffle()
    if (String(tempWord) == word) {
        tempWord.shuffle()
    }
    return String(tempWord)
}

fun guessWord(wordToGuess: String) {
    var userAttempts = 0
    tag@ while (userAttempts < 8) {
        val userGuess = readln()
        if (userGuess.equals(wordToGuess, ignoreCase = true)) {
            println("Hey! Indeed, the given word was $userGuess. You guessed in $userAttempts attempts")
            break@tag
        } else {
            println("Nope")
            userAttempts++
            if (userAttempts == ATTEMPTS) {
                println("You didn't guess the word. The answer is $wordToGuess")
                break@tag
            }
            continue@tag
        }
    }
}