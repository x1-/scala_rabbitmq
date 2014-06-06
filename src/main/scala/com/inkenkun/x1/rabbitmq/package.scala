package com.inkenkun.x1

import scala.concurrent.duration._

import akka.actor.ActorSystem
import com.github.sstone.amqp.ConnectionOwner
import com.rabbitmq.client.{Address, ConnectionFactory}

package object rabbitmq {

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
    new Address( "192.168.0.101", 5672 )
  )

  lazy val amqpActor = system.actorOf(
    ConnectionOwner.props(
      connFactory = connection,
      reconnectionDelay = 500 millis,
      addresses = Some( addresses ) ) )
}
