package com.inkenkun.x1.rabbitmq

import scala.concurrent.duration._

import org.specs2.mock.Mockito
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.time.NoTimeConversions

import akka.actor.Props
import com.github.sstone.amqp.Amqp.{Ack, Delivery}
import com.rabbitmq.client.Envelope
import com.rabbitmq.client.AMQP.BasicProperties

class SampleConsumerSpec extends SpecificationWithJUnit with Mockito with NoTimeConversions {
  sequential
  "SampleConsumer" should {
    "When Actor received `Delivery` message, return `Ack`" in new AkkaTestkitSpecs2Support {

      within(10 second) {

        val actor = system.actorOf( Props[SampleConsumer], name = "Sample" )

        val envelope = new Envelope( 0, false, "ex", "rt" )
        val prop = new BasicProperties()

        actor ! Delivery( "tag", envelope, prop, "test".getBytes() )
        expectMsgClass( classOf[Ack] )
      }
    }
  }

}
