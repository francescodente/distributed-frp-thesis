override def nbr[A](a: Flow[A]): Flow[NeighborField[A]] =
  Flows.of { path =>
    val alignmentPath = path :+ Nbr
    val neighboringValues = alignWithNeighbors(
      alignmentPath,
      (e, _) => e.root.asInstanceOf[A]
    )
    lift(a.run(alignmentPath), neighboringValues){ (exp, n) =>
      val neighborField = n + (ctx.selfId -> exp.root)
      ExportTree(neighborField, Nbr -> exp)
    }
  }