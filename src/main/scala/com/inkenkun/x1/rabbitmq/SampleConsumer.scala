package com.inkenkun.x1.rabbitmq

import akka.actor.{Actor, ActorLogging}
import com.github.sstone.amqp.Amqp.{Ack, Delivery}

class SampleConsumer extends Actor with ActorLogging {

  def receive: Receive = {
    case Delivery( consumerTag, envelope, properties, body ) => {
      val source = new String( body, "UTF-8" )
      log.info( s"receive message: ${source}" )
      sender ! Ack( envelope.getDeliveryTag )
    }
  }
}