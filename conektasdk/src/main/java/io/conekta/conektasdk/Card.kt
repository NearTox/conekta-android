package io.conekta.conektasdk

data class Card(
    val name: String,
    val number: String,
    val cvc: String,
    val expMonth: String,
    val expYear: String
) {
  // not used now
  // private val deviceFingerprint: String = ""

  init {
    if(name.isEmpty())
      throw RuntimeException("name")
    if(number.isEmpty())
      throw RuntimeException("number")
    if(cvc.isEmpty())
      throw RuntimeException("cvc")
    if(Integer.parseInt(expMonth) < 1 || Integer.parseInt(expMonth) > 12)
      throw RuntimeException("expMonth")
    if(Integer.parseInt(expYear) < 1)
      throw RuntimeException("expYear")
  }
}
