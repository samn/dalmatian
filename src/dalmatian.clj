(ns dalmatian
  (:require [twitter.oauth :as oauth]
            [twitter.api.streaming :as streaming]
            [cheshire.core :as json])
  (:import (twitter.callbacks.protocols AsyncStreamingCallback)))

(defn die!
  ([] (die! 1))
  ([code] (System/exit code)))

(defn env
  "Retrieve the value of var-name from the system environment, or nil"
  [var-name]
  (System/getenv var-name))

(def creds (oauth/make-oauth-creds 
             (env "APP_CONSUMER_KEY")
             (env "APP_CONSUMER_SECRET")
             (env "USER_ACCESS_TOKEN")
             (env "USER_ACCESS_TOKEN_SECRET")))

(defn on-bodypart
  "Called when a new message is received from the streaming api"
  [response baos]
  (let [tweet (json/parse-string (.toString baos) true)]
    tweet))

(defn on-failure
  "Called when the streaming api returns a 4xx response.
   Kill this process and let Heroku bounce it to reconnect."
  [response]
  (println response)
  (die!))

(defn on-exception
  "Called when an exception is thrown.
   Kill this process and let Heroku bounce it to reconnect."
  [response throwable]
  (println (.toString throwable))
  (die!))

(def async-streaming-callback
  (AsyncStreamingCallback.
    on-bodypart
    on-failure
    on-exception))

(defn -main
  [& args]
  )
