trait Core:
  type context
  type Export[+_]
  type Path

  trait Flow[A]:
    def run(path: Path)(using Context): Cell[Export[A]]