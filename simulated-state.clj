;; simulated-state.clj
;; This file represents a simulated final state of the application's atoms
;; after a series of hypothetical user interactions.

(def simulated-counter 4)

(def simulated-users-credentials
  {;; User A's credentials
   "tokenAaaabbbcccdddeee" {:username "userA1b3d5" :password "passA5e7f9"},
   ;; User C's credentials (User B logged out)
   "tokenCfffggghhhiiijjj" {:username "userC2g4h6" :password "passC6i8j0"}
   })

(def simulated-active-sessions
  #{;; User A's active session token
    "tokenAaaabbbcccdddeee",
    ;; User C's active session token (User B logged out)
    "tokenCfffggghhhiiijjj"
    })

;; End of simulated state.
