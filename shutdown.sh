#!/bin/bash
pid=`ps aux | grep profit | awk '{if($11=="java") print $2}'`

if { $pid }
then
	echo "Stop profit server, pid: $pid."
	kill $pid
else
	echo "No profit server process found, may be killed already."
fi