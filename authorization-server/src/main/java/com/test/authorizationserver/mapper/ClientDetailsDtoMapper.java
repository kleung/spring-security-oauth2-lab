package com.test.authorizationserver.mapper;

import com.test.authorizationserver.model.dto.ClientDetailsDto;
import lombok.AllArgsConstructor;
import org.mapstruct.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class ClientDetailsDtoMapper {

    private JpaAuthorityMapper jpaAuthorityMapper;

    private List<String> mapGrantedAuthorityCollectionToList(Collection<GrantedAuthority> authorityCollection) {
        return (authorityCollection == null) ? List.of() :
                authorityCollection.stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .collect(Collectors.toList());
    }

    private List<String> mapStringSetToList(Set<String> stringSet) {
        return (stringSet == null) ? List.of() :
                stringSet.stream()
                        .collect(Collectors.toList());
    }

    private Set<String> mapStringListToSet(List<String> stringList) {
        return (stringList == null) ? Set.of() :
                stringList.stream()
                        .collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> mapGrantedAuthorityListToCollection(List<String> authorityList) {
        if (authorityList == null) {
            return List.of();
        } else {
            return authorityList.stream()
                    .map(authority -> jpaAuthorityMapper.mapAuthority(authority))
                    .collect(Collectors.toList());
        }
    }

    public ClientDetailsDto mapClientDetails(ClientDetails clientDetails) {
        if (clientDetails == null) {
            return null;
        }

        ClientDetailsDto clientDetailsDto = new ClientDetailsDto();

        clientDetailsDto.setAuthorizedGrantTypes(mapStringSetToList( clientDetails.getAuthorizedGrantTypes()));
        clientDetailsDto.setClientId(clientDetails.getClientId());
        clientDetailsDto.setScope(mapStringSetToList(clientDetails.getScope()));
        clientDetailsDto.setRegisteredRedirectUri(mapStringSetToList(clientDetails.getRegisteredRedirectUri()));
        clientDetailsDto.setAuthorities(mapGrantedAuthorityCollectionToList(clientDetails.getAuthorities()));
        clientDetailsDto.setResourceIds(mapStringSetToList(clientDetails.getResourceIds()));

        return clientDetailsDto;
    }

    public List<ClientDetailsDto> mapClientDetailsList(List<ClientDetails> clientDetailsList) {
        if (clientDetailsList == null) {
            return null;
        }

        List<ClientDetailsDto> list = new ArrayList<ClientDetailsDto>(clientDetailsList.size());
        for (ClientDetails clientDetails : clientDetailsList) {
            list.add(mapClientDetails(clientDetails));
        }

        return list;
    }

    public BaseClientDetails mapClientDetailsDto(ClientDetailsDto clientDetailsDto) {
        if (clientDetailsDto == null) {
            return null;
        }

        BaseClientDetails baseClientDetails = new BaseClientDetails();

        baseClientDetails.setAuthorizedGrantTypes(mapStringListToSet(clientDetailsDto.getAuthorizedGrantTypes()));
        baseClientDetails.setClientId(clientDetailsDto.getClientId());
        baseClientDetails.setScope(mapStringListToSet(clientDetailsDto.getScope()));
        baseClientDetails.setRegisteredRedirectUri(mapStringListToSet(clientDetailsDto.getRegisteredRedirectUri()));
        baseClientDetails.setClientSecret(clientDetailsDto.getClientSecret());


        baseClientDetails.setAuthorities(mapGrantedAuthorityListToCollection(clientDetailsDto.getAuthorities()));

        baseClientDetails.setResourceIds(mapStringListToSet(clientDetailsDto.getResourceIds()));

        return baseClientDetails;
    }











}
