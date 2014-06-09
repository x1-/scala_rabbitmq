package com.inkenkun.x1.rabbitmq

import akka.actor.{Actor, ActorLogging, Props}
import com.github.sstone.amqp.{Amqp, ConnectionOwner, Consumer}
import com.github.sstone.amqp.Amqp.{AddBinding, Binding, QueueParameters, StandardExchanges}

class ConsumersStarter extends Actor with ActorLogging {

  /**
   * 下記をpackageオブジェクトに記載しています。
   *
  implicit val system = ActorSystem()
  implicit val config = system.settings.config

  lazy val connection: ConnectionFactory =
    ConnectionOwner.buildConnFactory(
      host = addresses.head.getHost,
      port = addresses.head.getPort,
      vhost = "/",
      user = "admin",
      password = "admin"
    )

  lazy val addresses: Array[Address] = Array(
    new Address( "192.168.0.101", 5672 ),
    new Address( "192.168.0.102", 5672 )
  )

  lazy val amqpActor = system.actorOf(
    ConnectionOwner.props(
      connFactory = connection,
      reconnectionDelay = 500 millis,
      addresses = Some( addresses ) ) )
   */

  def receive: Receive = {

    case "sample" =>
      val queueParams = queue( "sample-queue" )
      val child = ConnectionOwner.createChildActor(
        amqpActor,
        Consumer.props(
          listener = Some( system.actorOf( Props[SampleConsumer], name = "SampleConsumer" ) ),
          init     = List( AddBinding( Binding( StandardExchanges.amqDirect, queueParams, "route-key" ) ) )
        ),
        name = Some( "Sample" ) )

      Amqp.waitForConnection( system, child ).await()
  }

  def queue( name: String, passive: Boolean = false, durable: Boolean = true, exclusive: Boolean = false, autodelete: Boolean = true ): QueueParameters =
    QueueParameters(
      name = name,
      passive = passive,
      durable = durable,
      exclusive = exclusive,
      autodelete = autodelete )
}
