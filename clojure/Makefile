target/%.jar: src/mal/%.clj
	echo "leining $*"
	lein with-profile $* uberjar

clean:
	rm -r target/*
