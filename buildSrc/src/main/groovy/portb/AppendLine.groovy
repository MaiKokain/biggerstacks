package portb

@Category(StringBuilder)
class AppendLine {

    StringBuilder appendLine(CharSequence text) {
        return append(text + System.lineSeparator())
    }
}
