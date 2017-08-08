#!/bin/sh
set -eu

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
logloc="log/service.log"
mkdir -p -- `dirname "$logloc"`
touch "$logloc"
if command -v tmux > /dev/null 2>&1; then
	tmux split-window -v -c "#{pane_current_path}" tail -f "$logloc"
fi
java  \
	-Dlogback.configurationFile=../service.xml \
	-Djaxp.debug=1 \
	-Dfelix.fileinstall.dir="load" \
	-Dfelix.config.properties="file:conf/config.properties" \
	-jar bin/org.apache.felix.main*.jar
