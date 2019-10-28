package com.github.geirolz.advxml.error

import cats.data.Validated.{Invalid, Valid}
import cats.MonadError
import cats.data.{NonEmptyList, Validated}
import com.github.geirolz.advxml.error.exceptions.AggregatedException

import scala.util.Try

object ValidatedEx {

  def fromTry[A](t: Try[A]): ValidatedEx[A] =
    t.fold(e => Invalid(NonEmptyList.of(e)), Valid(_))

  def fromEither[A](e: Either[NonEmptyList[Throwable], A]): ValidatedEx[A] =
    Validated.fromEither(e)

  def fromOption[A](o: Option[A], ifNone: => NonEmptyList[Throwable]): ValidatedEx[A] =
    Validated.fromOption(o, ifNone)

  def transformNel[F[_], A](validated: ValidatedEx[A])(implicit F: MonadError[F, NonEmptyList[Throwable]]): F[A] = {
    validated match {
      case Valid(value) => F.pure(value)
      case Invalid(exs) => F.raiseError(exs)
    }
  }

  def transform[F[_], A](validated: ValidatedEx[A])(implicit F: MonadEx[F]): F[A] = {
    validated match {
      case Valid(value) => F.pure(value)
      case Invalid(exs) => F.raiseError(new AggregatedException(exs.toList))
    }
  }
}

private[advxml] trait ValidationSyntax {

  implicit class ValidatedExTryOps[A](t: Try[A]) {
    def toValidatedNel: ValidatedEx[A] = ValidatedEx.fromTry(t)
  }

  implicit class ValidatedExEitherOps[A](e: Either[Throwable, A]) {
    def toValidatedNel: ValidatedEx[A] =
      Validated.fromEither(e.left.map(NonEmptyList.of(_)))
  }

  implicit class ValidatedExEitherNelOps[A](e: Either[NonEmptyList[Throwable], A]) {
    def toValidatedNel: ValidatedEx[A] = ValidatedEx.fromEither(e)
  }

  implicit class ValidatedExOptionOps[A](e: Option[A]) {
    def toValidatedNel(ifNone: => NonEmptyList[Throwable]): ValidatedEx[A] = ValidatedEx.fromOption(e, ifNone)
  }

  implicit class ValidatedResOps[A](validated: ValidatedEx[A]) {

    def transformNel[F[_]](implicit F: MonadError[F, NonEmptyList[Throwable]]): F[A] =
      ValidatedEx.transformNel[F, A](validated)

    def transform[F[_]](implicit F: MonadEx[F]): F[A] =
      ValidatedEx.transform[F, A](validated)
  }
}
