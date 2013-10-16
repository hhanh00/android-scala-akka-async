package com.example.myfirstapp

import akka.actor.{Props, ActorSystem, Actor}
import scala.concurrent.duration._
import android.app.Activity
import android.os.{Handler, Bundle}
import android.view.Menu
import akka.pattern.ask
import akka.util.Timeout
import android.widget.TextView
import scala.concurrent.Await
import com.loopj.android.http.{AsyncHttpResponseHandler, AsyncHttpClient}

class MyApp extends android.app.Application {
  val system = ActorSystem()
  val httpActor = system.actorOf(Props(classOf[HttpActor]))
}

trait SetTextView {
  def setText(t: String): Unit
}

class MainActivity extends Activity with SetTextView {
  val handler = new Handler

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val myApp = getApplication.asInstanceOf[MyApp]
    myApp.httpActor ! HttpActor.Request(this)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.main, menu)
    return true
  }

  def setText(t: String)  {
    handler.post(new Runnable {
      override def run() {
        val textView = findViewById(R.id.hello).asInstanceOf[TextView]
        textView.setText(t)
      }
    })
  }
}

class HttpActor extends Actor {
  val client = new AsyncHttpClient()
  def receive = {
    case HttpActor.Request(textView) =>
      textView.setText("Request")
      client.get("http://www.google.com", new AsyncHttpResponseHandler() {
        override def onSuccess(response: String) {
          textView.setText(response)
        }
      })
      textView.setText("Request 2")
  }
}
object HttpActor {
  case class Request(textView: SetTextView)
}
