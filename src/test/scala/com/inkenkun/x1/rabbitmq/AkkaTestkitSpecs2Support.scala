package com.inkenkun.x1.rabbitmq

import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.ActorSystem
import org.specs2.mutable.After

/** for akka-testkit + specs2 */
abstract class AkkaTestkitSpecs2Support extends TestKit( ActorSystem() ) with After with ImplicitSender {
  def after = system.shutdown()
}