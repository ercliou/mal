target/%.jar: src/%.clj
	echo "leining $*"
	lein with-profile $* uberjar

clean:
	rm -r target/*
