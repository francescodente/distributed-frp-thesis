class SemanticsTests extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  private val SELF_ID = 1
  private val PATH = Seq.empty
  ...

  object SemanticsTestsIncarnation extends MockIncarnation:
    ...

  import SemanticsTestsIncarnation._
  import SemanticsTestsIncarnation.given

  private given ctx: Context = context(SELF_ID)

  override def beforeEach(): Unit = ctx.reset()

  "lift" should "combine two flows by nesting them" in {
    val left = "LEFT"
    val right = "RIGHT"
    val flow = lift(constant(left), constant(right))(_ + _)
    flow.run(PATH).sample() should be (ExportTree(
      left + right,
      Operand(0) -> ExportTree(left),
      Operand(1) -> ExportTree(right),
    ))
  }

  it should "react to changes in either its inputs" in {
    val stringOp = new CellSink("A")
    val intOp = new CellSink(2)
    val flow = lift(Flows.fromCell(stringOp), Flows.fromCell(intOp))(_ * _)
    val exports = flow.run(PATH)
    exports.sample().root should be ("AA")
    stringOp.send("B")
    exports.sample().root should be ("BB")
    intOp.send(3)
    exports.sample().root should be ("BBB")
  }
