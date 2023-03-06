trait IncarnationWithEnvironment(val environment: Environment):
  self: Incarnation =>

  override type DeviceId = Int