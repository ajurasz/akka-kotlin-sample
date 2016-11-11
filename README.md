## Kotlin with Akka example
First usage of Akka with Kotlin language

This simple app read file in CSV format then send each line to actor (`MongoQueryActor`) where it is processed. When line is processed it is passed to another actor (`OutputActor`).
Processing time is measured using `TimerActor`