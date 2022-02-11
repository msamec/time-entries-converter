(ns infrastructure.queue.proletarian
  (:require [domain.worker :refer [Worker -enqueue!]]
            [proletarian.worker :as worker]
            [proletarian.job :as job]
            [integrant.core :as ig]
            [next.jdbc :as jdbc]))

(defn enqueue! [ds job-type payload]
  (job/enqueue! ds job-type payload))

(defn handle-job! [jobs job-type payload]
  (let [job-handler (get jobs job-type)]
    (job-handler payload)))

(defmethod ig/init-key :infrastructure.queue/proletarian [_ {:keys [db config jobs]}]
  (let [ds (jdbc/get-datasource db)
        dc (jdbc/get-connection ds)
        worker (worker/create-queue-worker ds (partial handle-job! jobs) config)]
    (worker/start! worker)
    (reify Worker
      (-enqueue! [this job-type payload] (enqueue! dc job-type payload)))))

