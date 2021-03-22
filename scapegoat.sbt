import Scapegoat._

// The scapegoat sbt plugin adds intelligent settings and tasks for scapegoat,
// but doesn't allow inspections to be run on compile.
// This file directly adds the scalac plugin to enable compile time inspections,
// then configures scapegoat using the smart sbt settings and then transforms those
// settings and applies them to the scalac plugin too.
// Using values defined in Dependencies.scala, we can keep versions in sync.


addCompilerPlugin("com.sksamuel.scapegoat" %% "scalac-scapegoat-plugin" % scapegoatPluginV cross CrossVersion.full)

// Required by sbt-scapegoat.
ThisBuild / scapegoatVersion := scapegoatPluginV

// sbt-scapegoat sets this in the scapegoat scope.
// We still need to set it for the compile scope.
scalacOptions ++= Seq(
  s"-P:scapegoat:dataDir:${scapegoatOutputPath.value}", // Required by the scalac plugin.
)

scapegoatDisabledInspections := Seq(
  "VariableShadowing", // Too many false positives: https://github.com/sksamuel/scapegoat/issues/398
  "PartialFunctionInsteadOfMatch", // Not always safe.
)

// Convert sbt settings to scalac flags.
scalacOptions += scapegoatDisabledInspections.value.mkString("-P:scapegoat:disabledInspections:", ":", "")

// Upgrade all enabled inspections to error level.
scalacOptions += Seq(
  "ArrayEquals",
  "ArraysInFormat",
  "ArraysToString",
  "AsInstanceOf",
  "AvoidOperatorOverload",
  "AvoidSizeEqualsZero",
  "AvoidSizeNotEqualsZero",
  "AvoidToMinusOne",
  "BigDecimalDoubleConstructor",
  "BigDecimalScaleWithoutRoundingMode",
  "BoundedByFinalType",
  "BrokenOddness",
  "CatchException",
  "CatchFatal",
  "CatchNpe",
  "CatchThrowable",
  "ClassNames",
  "CollectionIndexOnNonIndexedSeq",
  "CollectionNamingConfusion",
  "CollectionNegativeIndex",
  "CollectionPromotionToAny",
  "ComparingFloatingPointTypes",
  "ComparingUnrelatedTypes",
  "ComparisonToEmptyList",
  "ComparisonToEmptySet",
  "ComparisonWithSelf",
  "ConstantIf",
  "DivideByOne",
  "DoubleNegation",
  "DuplicateImport",
  "DuplicateMapKey",
  "DuplicateSetValue",
  "EitherGet",
  "EmptyCaseClass",
  "EmptyFor",
  "EmptyIfBlock",
  "EmptyInterpolatedString",
  "EmptyMethod",
  "EmptySynchronizedBlock",
  "EmptyTryBlock",
  "EmptyWhileBlock",
  "ExistsSimplifiableToContains",
  "FilterDotHead",
  "FilterDotHeadOption",
  "FilterDotIsEmpty",
  "FilterDotSize",
  "FilterOptionAndGet",
  "FinalModifierOnCaseClass",
  "FinalizerWithoutSuper",
  "FindAndNotEqualsNoneReplaceWithExists",
  "FindDotIsDefined",
  "IllegalFormatString",
  "ImpossibleOptionSizeCondition",
  "IncorrectNumberOfArgsToFormat",
  "IncorrectlyNamedExceptions",
  "InvalidRegex",
  "IsInstanceOf",
  "JavaConversionsUse",
  "ListAppend",
  "ListSize",
  "LonelySealedTrait",
  "LooksLikeInterpolatedString",
  "MapGetAndGetOrElse",
  "MaxParameters",
  "MethodNames",
  "MethodReturningAny",
  "ModOne",
  "NanComparison",
  "NegationIsEmpty",
  "NegationNonEmpty",
  "NoOpOverride",
  "NullAssignment",
  "NullParameter",
  "ObjectNames",
  "OptionGet",
  "OptionSize",
  "ParameterlessMethodReturnsUnit",
  "PartialFunctionInsteadOfMatch",
  "PointlessTypeBounds",
  "PreferMapEmpty",
  "PreferSeqEmpty",
  "PreferSetEmpty",
  "ProductWithSerializableInferred",
  "PublicFinalizer",
  "RedundantFinalModifierOnMethod",
  "RedundantFinalModifierOnVar",
  "RedundantFinalizer",
  "RepeatedCaseBody",
  "RepeatedIfElseBody",
  "ReverseFunc",
  "ReverseTailReverse",
  "ReverseTakeReverse",
  "SimplifyBooleanExpression",
  "StripMarginOnRegex",
  "SubstringZero",
  "SuspiciousMatchOnClassObject",
  "SwallowedException",
  "SwapSortFilter",
  "TryGet",
  "TypeShadowing",
  "UnnecessaryConversion",
  "UnnecessaryIf",
  "UnnecessaryReturnUse",
  "UnreachableCatch",
  "UnsafeContains",
  "UnsafeStringContains",
  "UnsafeTraversableMethods",
  "UnusedMethodParameter",
  "UseCbrt",
  "UseExpM1",
  "UseLog10",
  "UseLog1P",
  "UseSqrt",
  "VarClosure",
  "VarCouldBeVal",
  "VariableShadowing",
  "WhileTrue",
  "ZeroNumerator",
).diff(scapegoatDisabledInspections.value)
  .mkString("-P:scapegoat:overrideLevels", "=Error:", "=Error")
