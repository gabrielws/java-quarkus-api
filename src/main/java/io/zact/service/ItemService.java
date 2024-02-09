package io.zact.service;

import io.zact.entity.ItemEntity;
import io.zact.model.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ItemService {

    public List<Item> get() { // Método para obter todos os itens
        List<ItemEntity> itemEntities = ItemEntity.listAll(); // Busca todos os itens no banco de dados e retorna uma lista
        return itemEntities.stream() // Converte a lista de entidades para uma lista de modelos
                .map(this::mapEntityToModel)
                .collect(Collectors.toList());
    }

    public Item getById(Long id) { // Método para obter um item pelo ID
        ItemEntity itemEntity = ItemEntity.findById(id); // Busca um item no banco de dados pelo ID
        if (itemEntity != null) { // Se o item foi encontrado, converte a entidade para um modelo e retorna
            return mapEntityToModel(itemEntity);
        } else {
            throw new EntityNotFoundException("O item com o ID fornecido não foi encontrado."); // Se o item não foi encontrado, lança uma exceção
        }
    }

    private Item mapEntityToModel(ItemEntity entity) { // Método para converter uma entidade para um modelo
        Item item = new Item();
        item.setId(entity.id);
        item.setName(entity.name);
        item.setCount(entity.count);
        item.setStatus(entity.status);
        return item;
    }

    public String mapItemsToString(List<Item> items) {
        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append("[");
            sb.append("ID:").append(item.getId()).append(", ");
            sb.append("Nome:").append(item.getName()).append(", ");
            sb.append("Count:").append(item.getCount()).append(", ");
            sb.append("Status:").append(item.getStatus());
            sb.append("], ");
        }
        return sb.toString();
    }

    @Transactional
    public void create(Item item) {
        // Verifica se algum dos campos está faltando
        if (item.getName() == null || item.getCount() == null || item.getStatus() == null) { // Se algum dos campos estiver faltando, lança uma exceção
            throw new ValidationException("Todos os campos (name, count e status) são obrigatórios.");
        }

        ItemEntity ie = new ItemEntity();
        ie.name = item.getName();
        ie.count = item.getCount();
        ie.status = item.getStatus();
        ie.persist();
    }

    @Transactional
    public void update(Item item) { // Método para atualizar um item existente
        if (item.getName() == null || item.getCount() == null || item.getStatus() == null) { // Se algum dos campos estiver faltando, lança uma exceção
            throw new ValidationException("Todos os campos (name, count e status) são obrigatórios.");
        }

        ItemEntity entity = ItemEntity.findById(item.getId());
        if (entity != null) {
            // Atualiza os campos
            entity.name = item.getName();
            entity.count = item.getCount();
            entity.status = item.getStatus();
        } else {
            throw new EntityNotFoundException("O item com o ID fornecido não foi encontrado.");
        }
    }

    @Transactional
    public void delete(Long id) { // Método para excluir um item pelo ID
        ItemEntity entity = ItemEntity.findById(id);
        if (entity == null) { // Se o item não foi encontrado, lança uma exceção
            throw new EntityNotFoundException("O item com o ID fornecido não existe.");
        }
        entity.delete();
    }
}