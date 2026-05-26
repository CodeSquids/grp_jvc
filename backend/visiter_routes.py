from flask import Blueprint, request, jsonify
from models.visiter import VisiterModel

visiter_bp = Blueprint('visiter', __name__)
visiter_model = VisiterModel()

@visiter_bp.route('/api/visiter', methods=['GET'])
def get_visiter():
    """Liste tous les visiter"""
    visiter = visiter_model.get_all()
    return jsonify(visiter)

@visiter_bp.route('/api/visiter/<n_visiter>', methods=['GET'])
def get_visiter_by_id(n_visiter):
    """Récupère un visiter par son ID"""
    visiter = visiter_model.get_by_id(n_visiter)
    if visiter:
        return jsonify(visiter)
    return jsonify({'error': 'Visiter non trouvé'}), 404

@visiter_bp.route('/api/visiter', methods=['POST'])
def create_visiter():
    """Crée un nouveau visiter"""
    data = request.json
    result = visiter_model.create(
        data.get('n_visiter'),
        data.get('n_visiteur'),
        data.get('n_site'),
        data.get('nbjours'),
        data.get('date_visite')
    )
    if result['success']:
        return jsonify(result), 201
    return jsonify(result), 400

@visiter_bp.route('/api/visiter/<n_visiter>', methods=['PUT'])
def update_visiter(n_visiter):
    """Met à jour un visiter"""
    data = request.json
    result = visiter_model.update(
        n_visiter,
        data.get('n_visiteur'),
        data.get('n_site'),
        data.get('nbjours'),
        data.get('date_visite')
    )
    if result['success']:
        return jsonify(result)
    return jsonify(result), 400

@visiter_bp.route('/api/visiter/<n_visiter>', methods=['DELETE'])
def delete_visiter(n_visiter):
    """Supprime un visiter"""
    result = visiter_model.delete(n_visiter)
    if result['success']:
        return jsonify(result)
    return jsonify(result), 400

# ===================== REQUÊTES COMPLEXES =====================

@visiter_bp.route('/api/visiter/complex1', methods=['POST'])
def complex1():
    """Requête complexe 1: Liste des visiteurs par site et période"""
    data = request.json
    site_nom = data.get('site_nom')
    date_start = data.get('date_start')
    date_end = data.get('date_end')
    
    if not date_start or not date_end:
        return jsonify({'error': 'date_start et date_end requis'}), 400
    
    resultats = visiter_model.complex1(site_nom, date_start, date_end)
    return jsonify(resultats)

@visiter_bp.route('/api/visiter/complex2', methods=['POST'])
def complex2():
    """Requête complexe 2: Statistiques par site et période"""
    data = request.json
    date_start = data.get('date_start')
    date_end = data.get('date_end')
    
    if not date_start or not date_end:
        return jsonify({'error': 'date_start et date_end requis'}), 400
    
    resultats = visiter_model.complex2(date_start, date_end)
    return jsonify(resultats)

@visiter_bp.route('/api/visiter/complex3', methods=['GET'])
def complex3():
    """Requête complexe 3: Liste complète des visiteurs"""
    resultats = visiter_model.complex3()
    return jsonify(resultats)

@visiter_bp.route('/api/visiter/complex4', methods=['GET'])
def complex4():
    """Requête complexe 4: Statistiques globales par site"""
    resultats = visiter_model.complex4()
    return jsonify(resultats)