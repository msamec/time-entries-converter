(defproject my-stuff "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "https://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-tools-deps "0.4.5"]]
  :main ^:skip-aot infrastructure.server
  :middleware     [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :uberjar-name  "app-standalone.jar"
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :provided {:lein-tools-deps/config {:config-files [:install :user :project "deps.edn"]}}})
