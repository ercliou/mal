target/%.jar: src/mal/%.clj deps
	echo "leining $*"
	lein with-profile $* uberjar

deps: src/mal/reader.clj \
	src/mal/printer.clj \
	src/mal/env.clj \
	src/mal/eval.clj

clean:
	rm -r target/*
