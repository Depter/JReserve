format 76

activityactioncanvas 128002 activityaction_ref 138498 // activity action Set seed
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 165 555.5 2000 113 41
end
activityactioncanvas 128130 activityaction_ref 138370 // activity action Select simulation count
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 172 481.5 2000 101 41
end
activityactioncanvas 128258 activityaction_ref 138242 // activity action Exclude errors
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 172 408.5 2000 103 41
end
activityactioncanvas 128386 activityaction_ref 138114 // activity action Select method
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 165 43.5 2000 107 41
end
activitynodecanvas 128514 activitynode_ref 135938 // activity_final
  xyz 210 656.5 2000
end
activitynodecanvas 128642 activitynode_ref 135810 // initial_node
  xyz 69 55.5 2000
end
activitynodecanvas 129410 activitynode_ref 136066 // decision
  xyz 205 123 2000
end
activityactioncanvas 129666 activityaction_ref 138626 // activity action Constant scale
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 86 213 2000 94 40
end
activityactioncanvas 129794 activityaction_ref 138754 // activity action Variable scale
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 266 210 2000 98 40
end
activitynodecanvas 130178 activitynode_ref 136194 // merge
  xyz 210 321 2005
end
flowcanvas 128898 flow_ref 140290 // <flow>
  
  from ref 128130 z 2001 to ref 128002
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129026 flow_ref 139906 // <flow>
  
  from ref 128642 z 2001 to ref 128386
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129154 flow_ref 140162 // <flow>
  
  from ref 128258 z 2001 to ref 128130
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129282 flow_ref 140418 // <flow>
  
  from ref 128002 z 2001 to ref 128514
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129538 flow_ref 140546 // <flow>
  
  from ref 128386 z 2001 to ref 129410
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129922 flow_ref 140674 // <flow>
  geometry VHr
  
  from ref 129410 z 2001 to point 130 139
  line 130818 z 2001 to ref 129666
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130050 flow_ref 140802 // <flow>
  geometry HV
  
  from ref 129410 z 2001 to point 312 139
  line 130690 z 2001 to ref 129794
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130306 flow_ref 140930 // <flow>
  geometry VH
  
  from ref 129666 z 2006 to point 130 337
  line 131074 z 2006 to ref 130178
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130434 flow_ref 141058 // <flow>
  geometry HVr
  
  from ref 129794 z 2006 to point 312 337
  line 131202 z 2006 to ref 130178
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 130562 flow_ref 141186 // <flow>
  
  from ref 130178 z 2006 to ref 128258
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
end
