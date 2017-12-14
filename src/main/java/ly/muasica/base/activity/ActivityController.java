package ly.muasica.base.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ly.muasica.base.activity.structure.Tag;
import ly.muasica.base.activity.structure.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activity")
public class ActivityController {
  
  @Autowired
  private ActivityRepository activityRepository;
  
  @Autowired
  private TagRepository tagRepository;
  
  
  @GetMapping
  public ArrayList<Activity> listAll() {
      ArrayList<Activity> activities = new ArrayList<>();
      activityRepository.findAll().forEach(activity -> activities.add(activity));
      return activities;
  }

  @GetMapping("{id}")
  public Activity find(@PathVariable Long id) {
      return activityRepository.findOne(id);
  }

  @PostMapping
  public Activity create(@RequestBody Activity input) {
      ArrayList<Tag> tags = cleanTags(input.getTags());
      return activityRepository.save(new Activity(input.getText(), input.getTitle(), tags));
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Long id) {
      activityRepository.delete(id);
  }

  @PutMapping("{id}")
  public Activity update(@PathVariable Long id, @RequestBody Activity input) {
      Activity activity = activityRepository.findOne(id);
      if (activity == null) {
          return null;
      } else {
          activity.setText(input.getText());
          activity.setTitle(input.getTitle());
          activity.setTags(cleanTags(input.getTags()));
          return activityRepository.save(activity);
      }
  }
  
  @GetMapping("/filter/{name}")
  public List<Activity> filter(@PathVariable String name)  {
      ArrayList<Activity> activities = listAll();

      List<Activity> filtered = activities
              .stream().filter(activity -> activity.getTags().stream()
                      .anyMatch(tag -> tag.getName().toLowerCase().equals(name.toLowerCase()))
                      )
              .collect(Collectors.toList());
      return filtered;
  }
  
  private ArrayList<Tag> cleanTags(ArrayList<Tag> tags)  {
      ArrayList<String> checked = new ArrayList<>();  // Prevent Duplications
      ArrayList<Tag> cleanedTags = new ArrayList<>();
      tags.stream().forEach(tag -> {
          String name = tag.getName();
          if (name.contains(Tag.prefix))  {
              name = name.replace(Tag.prefix, "");  // removes all Tag Prefixes
          }
          if (!checked.contains(name))  {
              Tag toFind = tagRepository.findOne(name);
              if (toFind == null)  {
                  Tag newTag = new Tag(name);
                  tagRepository.save(newTag);
                  cleanedTags.add(newTag);
              }  else  {
                  cleanedTags.add(toFind);
              }
              checked.add(name);
          }
      });
      
      return cleanedTags;
  }
}