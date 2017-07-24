dir=extract
if [ -d "$dir" ]; then
  rm -rf -- "$dir"
fi

mkdir -p "$dir/load"
cd "$dir"

unzip -o `find .. -name '*.zip'`

if [ -d felix-cache ]; then
  rm -rf -- felix-cache
fi

echo Starting...
java  \
	-Djaxp.debug=1 \
	-Dfelix.fileinstall.dir="load" \
	-Dfelix.config.properties="file:conf/config.properties" \
	-jar bin/org.apache.felix.main*.jar

