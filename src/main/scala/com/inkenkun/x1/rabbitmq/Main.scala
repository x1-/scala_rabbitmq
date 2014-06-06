package com.inkenkun.x1.rabbitmq

import scala.concurrent.duration._

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

object Main extends App {

/**
 * 下記をpackageオブジェクトに記載しています。
 *
  implicit val system = ActorSystem()
  implicit val config = system.settings.config
 */

  import system.dispatcher
  implicit val timeout: Timeout = 5.second

  // コンシューマーを開始して待ち受けます。
  val starter = system.actorOf( Props[ConsumersStarter], name="ConsumersStarter" )
  starter ! "sample"

  // パブリッシャーでRabbitMQにメッセージを送ります。
  val producer = system.actorOf( Props[Producer], name="Producer" )
  producer ? Producer.Start onSuccess {
    case Producer.Ok => producer ! Producer.Standard( "route-key", "test".getBytes )
  }
}
