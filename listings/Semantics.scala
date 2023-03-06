trait Semantics:
  self: Core with Language =>

  type NeighborState <: BasicNeighborState
  override type Context <: BasicContext
  override type NeighborField[+A] = Map[DeviceId, A]
  override type Export[+A] = ExportTree[A]
  override type Path = Seq[Slot]

  trait BasicNeighborState:
    def sensor[A](id: NeighborSensorId): A
    def exported: Export[Any]

  trait BasicContext:
    def selfId: DeviceId
    def sensor[A](id: LocalSensorId): Cell[A]
    def neighbors: Cell[Map[DeviceId, NeighborState]]

  ...
  
case class ExportTree[+A](root: A, children: Map[Slot, ExportTree[Any]]):
  def followPath(path: Seq[Slot]): Option[ExportTree[Any]] = path match
    case h :: t => children.get(h).flatMap(_.followPath(t))
    case _ => Some(this)

enum Slot:
  case Operand(index: Int)
  case Nbr
  case Condition
  case Then
  case Else
  case Key[T](value: T)