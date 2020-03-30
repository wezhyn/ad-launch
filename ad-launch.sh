#!/bin/bash


admain=deployment.apps/dep-ad-main-server
screen=deployment.apps/dep-screen-server
kubectl apply -f ad-launch.yaml
kubectl patch $admain -p   "{\"spec\":{\"template\":{\"metadata\":{\"annotations\":{\"date\":\"`date +'%s'`\"}}}}}"
kubectl patch $screen -p   "{\"spec\":{\"template\":{\"metadata\":{\"annotations\":{\"date\":\"`date +'%s'`\"}}}}}"

#a=$(kubectl apply -f ad-launch.yaml)
#OLD_IFS="$IFS"
#IFS=" "
#arr=($a)
#IFS="$OLD_IFS"
#i=0
#while  [[ i -lt "${#arr[@]}" ]];do
#  str="${arr[$i]}"
#  if [[ "$str" =~ .*"$admain".*  ]]; then
#    ((i++));
#    str="${arr[$i]}"
#    if [[ "$str" =~  .*"unchanged".* ]]; then
#    fi
#  fi
#  if [[ "$str" =~ .*"$screen".*  ]]; then
#    ((i++));
#    str="${arr[$i]}"
#    if [[ "$str" =~  .*"unchanged".* ]]; then
#    fi
#  fi
#    ((i++));
#done
#
