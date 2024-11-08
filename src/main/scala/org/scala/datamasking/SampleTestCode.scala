package org.scala.datamasking

import com.privacylogistics.FF3Cipher

object SampleTestCode extends App {

  val key = "2DE79D232DF5585D68CE47882AE256D6"
  val tweak= "CBD09280979564"
  val plaintext = "BADD@gmail"

  val DIGITS = "0123456789"
  val LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ "
  val STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~"
  val EMAIL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@._"

  // message length within min 4 and max 28 bounds
  // println(""+new FF3Cipher(key, tweak, STRING).encrypt(plaintext))
  // message length within min 6 and max 56 bounds
  //println(""+new FF3Cipher(key, tweak, DIGITS).encrypt(plaintext))
  // message length within min 4 and max 32 bounds
  //println(""+new FF3Cipher(key, tweak, LETTERS).encrypt(plaintext))
  // message length within min 4 and max 30 bounds
  println(""+new FF3Cipher(key, tweak, EMAIL).encrypt(plaintext))


}
