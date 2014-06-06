package com.inkenkun.x1.rabbitmq

import scala.concurrent.duration._

import org.specs2.mock.Mockito
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.time.NoTimeConversions

import akka.actor.{ActorRef, Props}
import com.github.sstone.amqp.Amqp.Publish

/** Actor using Mock */
class ProducerMock( actor: ActorRef ) extends Producer {
  override lazy val producer = actor
}

class ProducerSpec extends SpecificationWithJUnit with Mockito with NoTimeConversions {
  sequential
  "Producer" should {
    "When Actor received `Producer.Standard` message, return `Publish`" in new AkkaTestkitSpecs2Support {

      val actor = system.actorOf( Props( classOf[ProducerMock], testActor ) )

      within(10 second) {

        val message = Producer.Standard(
          "route-key",
          "12345".getBytes )

        actor ! message
        expectMsgClass(classOf[Publish])
      }
    }
  }
}
