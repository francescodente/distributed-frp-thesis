def gradient(src: Boolean): Double =
  rep(Double.PositiveInfinity) { distance =>
    mux(src) {
      0.0
    } {
      minHoodPlus(nbr(distance) + nbrRange)
    }
  }