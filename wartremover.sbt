// This files contains all the build settings for wartremover.
// It also pulls in additional inspections from the wartremover-contrib package.
// Some inspections (warts) are disabled because they're duplicated by inspections
// in scapegoat and scalastyle and scapegoat is more performant/better and scalastyle
// shows better information in intellij.

wartremoverErrors ++= Warts.allBut(
  Wart.Any, // Too many false positives.
  Wart.Nothing, // Too many false positives.
  Wart.OptionPartial, // Already covered better by scapegoat.
  Wart.TraversableOps, // Already covered better by scapegoat.
  Wart.TryPartial, // Already covered better by scapegoat.
)

wartremoverErrors ++= ContribWart.allBut(
  ContribWart.SealedCaseClass, // sealed case classes are useful for smart constructors.
  ContribWart.SymbolicName, // Too many false positives
)
