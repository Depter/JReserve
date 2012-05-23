format 76

activitynodecanvas 128002 activitynode_ref 135810 // initial_node
  xyz 34 46 2000
end
activityactioncanvas 128130 activityaction_ref 138114 // activity action Select method
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 127 37 2000 107 40
end
activityactioncanvas 128258 activityaction_ref 138242 // activity action Exclude errors
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 131 121 2000 102 40
end
activityactioncanvas 128386 activityaction_ref 138370 // activity action Select simulation count
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 131 194 2000 100 41
end
activityactioncanvas 128898 activityaction_ref 138498 // activity action Set seed
  show_infonote default drawing_language default show_stereotype_properties default
  show_opaque_action_definition default
  xyzwh 123 268 2000 113 40
end
activitynodecanvas 129154 activitynode_ref 135938 // activity_final
  xyz 168 369 2000
end
flowcanvas 128514 flow_ref 139906 // <flow>
  
  from ref 128002 z 2001 to ref 128130
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 128642 flow_ref 140034 // <flow>
  
  from ref 128130 z 2001 to ref 128258
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 128770 flow_ref 140162 // <flow>
  
  from ref 128258 z 2001 to ref 128386
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129026 flow_ref 140290 // <flow>
  
  from ref 128386 z 2001 to ref 128898
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
flowcanvas 129282 flow_ref 140418 // <flow>
  
  from ref 128898 z 2001 to ref 129154
  show_infonote default drawing_language default show_stereotype_properties default write_horizontally default
end
end
