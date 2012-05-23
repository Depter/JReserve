format 76

activityactioncanvas 128002 activityaction_ref 137730 // activity action Select ratios
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 278 8.5 2000 105 41
end
activitynodecanvas 128130 activitynode_ref 128642 // activity_final
  xyz 505 589 2000
end
activityactioncanvas 128386 activityaction_ref 129794 // activity action Comment development factors
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 382 348 2000 101 61
end
activityactioncanvas 128514 activityaction_ref 129922 // activity action Select fitted factors
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 551 348 2000 101 61
end
activityactioncanvas 128642 activityaction_ref 130050 // activity action Comment method
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 141 474 2000 105 41
end
activityactioncanvas 128770 activityaction_ref 130178 // activity action Set estimate name
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 297 473 2000 97 41
end
activityactioncanvas 128898 activityaction_ref 130306 // activity action Save estimate
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 468 473 2000 97 41
end
activityactioncanvas 129026 activityaction_ref 129282 // activity action Select triangle
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 113 8 2000 101 41
end
activityactioncanvas 129154 activityaction_ref 129410 // activity action Exclude individual factor
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 112 87 2000 101 61
end
activityactioncanvas 129282 activityaction_ref 129538 // activity action Comment excluded factor
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 112 184 2000 101 61
end
activityactioncanvas 129410 activityaction_ref 129666 // activity action Select development factors
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 228 347 2000 101 61
end
activitynodecanvas 129538 activitynode_ref 128386 // initial_node
  xyz 24 19 2000
end
activitynodecanvas 129666 activitynode_ref 128514 // decision
  xyz 149 285 2000
end
flowcanvas 129794 flow_ref 129922 // <flow>
  
  from ref 129154 z 2001 to ref 129282
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129922 flow_ref 130562 // <flow>
  
  from ref 128386 z 2001 to ref 128514
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130050 flow_ref 130050 // <flow>
  
  from ref 129282 z 2001 to ref 129666
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130306 flow_ref 131074 // <flow>
  
  from ref 128898 z 2001 to ref 128130
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130434 flow_ref 130690 // <flow>
  geometry VHV
  
  from ref 128514 z 2001 to point 598 439
  line 130562 z 2001 to point 191 439
  line 130690 z 2001 to ref 128642
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130818 flow_ref 130946 // <flow>
  
  from ref 128770 z 2001 to ref 128898
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130946 flow_ref 129666 // <flow>
  
  from ref 129538 z 2001 to ref 129026
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 131074 flow_ref 138882 // <flow>
  geometry VHV
  
  from ref 128002 z 2001 to point 326 65
  line 131202 z 2001 to point 160 65
  line 131330 z 2001 to ref 129154
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 131458 flow_ref 130306 // <flow>
  geometry VH
  
  from ref 129666 z 2001 to point 158 374
  line 131586 z 2001 to ref 129410
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 131714 flow_ref 130178 // <flow>
  
  from ref 129666 z 2001 to point 265 300
  line 131842 z 2001 to point 265 112
  line 131970 z 2001 to ref 129154
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 132098 flow_ref 130434 // <flow>
  
  from ref 129410 z 2001 to ref 128386
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 132226 flow_ref 130818 // <flow>
  
  from ref 128642 z 2001 to ref 128770
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 132610 flow_ref 139010 // <flow>
  
  from ref 129026 z 2001 to ref 128002
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
end
