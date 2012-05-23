format 76

activityactioncanvas 128002 activityaction_ref 129666 // activity action Select development factors
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 237 359.5 2000 101 61
end
activitynodecanvas 128130 activitynode_ref 128386 // initial_node
  xyz 33 30.5 2000
end
activityactioncanvas 128258 activityaction_ref 129538 // activity action Comment excluded factor
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 121 196.5 2000 101 61
end
activityactioncanvas 128386 activityaction_ref 129410 // activity action Exclude individual factor
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 121 99.5 2000 101 61
end
activityactioncanvas 128514 activityaction_ref 129282 // activity action Select triangle
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 122 19.5 2000 101 41
end
activitynodecanvas 128642 activitynode_ref 128514 // decision
  xyz 158 297.5 2000
end
activitynodecanvas 128770 activitynode_ref 128642 // activity_final
  xyz 514 601.5 2000
end
activityactioncanvas 128898 activityaction_ref 130306 // activity action Save estimate
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 477 485.5 2000 97 41
end
activityactioncanvas 129026 activityaction_ref 130178 // activity action Set estimate name
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 306 485.5 2000 97 41
end
activityactioncanvas 129154 activityaction_ref 130050 // activity action Comment method
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 150 486.5 2000 105 41
end
activityactioncanvas 129282 activityaction_ref 129922 // activity action Select fitted factors
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 560 360.5 2000 101 61
end
activityactioncanvas 129410 activityaction_ref 129794 // activity action Comment development factors
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 391 360.5 2000 101 61
end
activityactioncanvas 131714 activityaction_ref 137602 // activity action Select exposure
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 273 20 2000 105 40
end
activityactioncanvas 131842 activityaction_ref 137730 // activity action Select ratios
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 437 19 2000 105 40
end
flowcanvas 129666 flow_ref 131074 // <flow>
  
  from ref 128898 z 2001 to ref 128770
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129794 flow_ref 129922 // <flow>
  
  from ref 128386 z 2001 to ref 128258
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129922 flow_ref 130306 // <flow>
  geometry VH
  
  from ref 128642 z 2001 to point 167 386
  line 130050 z 2001 to ref 128002
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130178 flow_ref 130818 // <flow>
  
  from ref 129154 z 2001 to ref 129026
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130306 flow_ref 129666 // <flow>
  
  from ref 128130 z 2001 to ref 128514
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130434 flow_ref 130050 // <flow>
  
  from ref 128258 z 2001 to ref 128642
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130562 flow_ref 130434 // <flow>
  
  from ref 128002 z 2001 to ref 129410
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130690 flow_ref 130946 // <flow>
  
  from ref 129026 z 2001 to ref 128898
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130818 flow_ref 130562 // <flow>
  
  from ref 129410 z 2001 to ref 129282
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130946 flow_ref 130178 // <flow>
  
  from ref 128642 z 2001 to point 274 313
  line 131074 z 2001 to point 274 125
  line 131202 z 2001 to ref 128386
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 131330 flow_ref 130690 // <flow>
  geometry VHV
  
  from ref 129282 z 2001 to point 607 451
  line 131458 z 2001 to point 200 451
  line 131586 z 2001 to ref 129154
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 131970 flow_ref 138626 // <flow>
  
  from ref 128514 z 2001 to ref 131714
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 132098 flow_ref 138754 // <flow>
  
  from ref 131714 z 2001 to ref 131842
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 132226 flow_ref 138882 // <flow>
  geometry VHV
  
  from ref 131842 z 2001 to point 487 76
  line 132354 z 2001 to point 169 76
  line 132482 z 2001 to ref 128386
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
end
