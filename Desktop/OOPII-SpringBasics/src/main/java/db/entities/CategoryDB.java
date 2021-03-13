package db.entities;

import api.foursquare.entities.Category;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Class that represents a Foursquare category in our database schema.
 * @author delta
 */
@Entity
@Table(name = "category")
public class CategoryDB implements Serializable
{
    private static final long serialVersionUID = -7047292240228252349L;

    @Id
    @Column(name = "category_id" , length = 24)
    private String category_id;

    @Column(name = "category_name", length = 50)
    private String name;

    @Column(name = "category_plural_name" , length = 50)
    private String pluralName;

    @Column(name = "url_icon" , length = 200)
    private String url;

    @Column(name = "parent_category" , length = 24)
    @JoinColumn(name = "parent_category", referencedColumnName = "category_id")
    private String parentID;

    public String getCategory_id()
    {
        return category_id;
    }
    public String getName()
    {
        return name;
    }
    public String getPluralName()
    {
        return pluralName;
    }
    public String getUrl()
    {
        return url;
    }
    public String getParentID()
    {
        return parentID;
    }

    public void setCategory_id(String id)
    {
        this.category_id = id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setPluralName(String pluralName)
    {
        this.pluralName = pluralName;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }
    public void setParentID(String parentID)
    {
        this.parentID = parentID;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDB that = (CategoryDB) o;
        return category_id.equals(that.category_id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(category_id);
    }

    /**
     * Class that contains utilities for converting Foursquare entities into
     * their DB equivalent ones.
     * @author delta
     */
    public static class FoursquareCategoryFilter
    {

        /**
         * Converts an Foursquare category entity into it's DB equivalent one.
         * @param category the Foursquare category
         * @param parentID the ID of the category in the tree's root.
         * @return a CategoryDB entity.
         */
        public static CategoryDB filter(Category category , String parentID)
        {
            CategoryDB ent = new CategoryDB();
            ent.setCategory_id(category.getId());
            ent.setName(category.getName());
            ent.setPluralName(category.getPluralName());
            ent.setUrl(category.getIcon().getUri().toString());
            ent.setParentID(parentID);

            return ent;
        }

        /**
         * Converts the Foursquare category hierarchy tree forest into a LinkedHashSet with their equivalent DB entities.
         * @param categories the Foursquare category tree forest
         * @return the LinkedHashSet.
         */
    /*
        We achieve 3 things with this method:
        1. We convert the tree forest to a set. In our implementation we don't care about the exact hierarchy
           but only for the root father
        2. We "filter" our data : We keep only what we need.
        3. Without a LinkedHashSet, our database schema would have difficulty inserting the data.
           We need the main category to be inserted first.
     */
        public static LinkedHashSet<CategoryDB> filter(ArrayList<Category> categories)
        {
            LinkedHashSet<CategoryDB> categoriesDB = new LinkedHashSet<>();

            for(Category catObj : categories)
                categoriesDB.addAll(BFStraversal(catObj));

            return categoriesDB;
        }

        /**
         * A simple BFS implementation used for traversing the whole tree.
         * @param cat the root of the tree
         * @return a LinkedHashSet with all the categories of the tree, keeping the row of the traversal
         */

    /*
        Why is BFS the ideal algorithm ? Because it has level-order traversal. DFS would NOT be suitable.
     */
        private static LinkedHashSet<CategoryDB> BFStraversal(Category cat)
        {
            //Create a LinkedHashSet to keep the traversal order
            LinkedHashSet<CategoryDB> catDB = new LinkedHashSet<>();

            // Create a queue for BFS
            LinkedList<Category> queue = new LinkedList<>();

            //Create a HashSet that holds if a node has been visited
            HashSet<Category> nodeInfo = new HashSet<>();

            Category current_Source = cat;

            //We hold the id of the root category
            String parentID = cat.getId();

            // Mark the current node as visited and enqueue it
            nodeInfo.add(current_Source);
            queue.add(current_Source);

            while (queue.size() != 0)
            {
                // Dequeue a category from queue
                current_Source = queue.poll();

                CategoryDB ent = filter(current_Source,parentID);

                catDB.add(ent);

                // Get all the children of the source
                List<Category> children = current_Source.getSubCategories();

                //If that source doesn't have any children then continue
                if (children.isEmpty())
                    continue;

                for(Category child : children)
                {
                    if ( !nodeInfo.contains(child) )
                    {
                        //Add the child as visited
                        nodeInfo.add(child);
                        //Add the child to the queue
                        queue.add(child);
                    }
                }
            }

            return catDB;
        }
    }
}
