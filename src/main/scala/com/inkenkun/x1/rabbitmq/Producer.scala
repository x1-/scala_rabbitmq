package com.inkenkun.x1.rabbitmq

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging}
import com.github.sstone.amqp.{Amqp, ChannelOwner, ConnectionOwner}
import com.github.sstone.amqp.Amqp.{Publish, StandardExchanges}
import com.rabbitmq.client.AMQP

class Producer extends Actor with ActorLogging {

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

  lazy val producer = ConnectionOwner.createChildActor( amqpActor, ChannelOwner.props() )

  def receive: Receive = {
    case Producer.Start =>
      Amqp.waitForConnection( system, amqpActor, producer ).await( 5, TimeUnit.SECONDS )
      sender ! Producer.Ok

    case Producer.Standard( key, body ) =>
      producer ! Publish(
        exchange = StandardExchanges.amqDirect.name,
        key = key,
        body = body,
        properties = Some( new AMQP.BasicProperties.Builder().deliveryMode(2).build() ),
        mandatory = true,
        immediate = false )
  }
}

object Producer {
  case object Start
  case object Ok
  case class Standard( routeKey: String, body: Array[Byte] )
}