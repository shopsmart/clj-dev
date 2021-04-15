# **DEPRECATED** clj-dev

Clojure tools making development and exploration more friendly

# breakpoint.clj

Support for adding breakpoints to Clojure code that execute a REPL in the surrounding lexical scope.

Usage:

```clojure
(breakpoint)
```

Note: Emacs doesn't like sub-repls; if you use Emacs: sorry. :-()


# mount.clj

Mount large Clojure data structures and navigate them using Unix filesystem-like commands.  Results are automatically paged in order to prevent crashing your REPL by printing too much information at once.

# Learning Clojure(script)

* https://github.com/bbatsov/clojure-style-guide
* https://github.com/magomimmo/modern-cljs/blob/master/doc/second-edition/tutorial-01.md


# Other resources

Other good tools for debugging:

* https://github.com/jonase/kibit
* https://github.com/dgrnbrg/spyscope
* http://docs.caudate.me/lucidity/

And debug tooling docs...

* http://brownsofa.org/blog/2014/08/03/debugging-in-clojure-tools/
* http://z.caudate.me/give-your-clojure-workflow-more-flow/
* http://dev.solita.fi/2014/03/18/pimp-my-repl.html
