package kis.data;

import java.util.List;

/**
 * DAO, реализующий работу с деревом
 *
 * @author yury
 */
public interface TreeDAO<T> {
    /**
     * спрашиваем всех детей первого уровня для заданного id.
     * Остальные дети должны подгружаться сами при помощи
     * hibernate и механизма lazy (или eager) load, т.к. их
     * фишка именно в этом
     *
     * @param id идентификатор родителя
     * @return дети первого уровня
     */
    public List<T> getSubtree(Long id);

    /**
     * Возвращает корневые элементы дерева, т.е. те, у которых
     * parent null
     * @return список, содержащий корневые элементы
     */
    public List<T> getRootElements();

    public List<T> getAllParents(Long id);
}
