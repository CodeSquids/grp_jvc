from flask import Blueprint, request, jsonify
from models.visiteur import VisiteurModel

visiteur_bp = Blueprint('visiteur', __name__)
visiteur_model = VisiteurModel()

@visiteur_bp.route('/api/visiteurs', methods=['GET'])
def get_visiteurs():
    """Liste tous les visiteurs"""
    visiteurs = visiteur_model.get_all()
    return jsonify(visiteurs)

@visiteur_bp.route('/api/visiteurs/<n_visiteur>', methods=['GET'])
def get_visiteur(n_visiteur):
    """Récupère un visiteur par son numéro"""
    visiteur = visiteur_model.get_by_id(n_visiteur)
    if visiteur:
        return jsonify(visiteur)
    return jsonify({'error': 'Visiteur non trouvé'}), 404

@visiteur_bp.route('/api/visiteurs', methods=['POST'])
def create_visiteur():
    """Crée un nouveau visiteur"""
    data = request.json
    result = visiteur_model.create(
        data.get('n_visiteur'),
        data.get('nom'),
        data.get('adresse')
    )
    if result['success']:
        return jsonify(result), 201
    return jsonify(result), 400

@visiteur_bp.route('/api/visiteurs/<n_visiteur>', methods=['PUT'])
def update_visiteur(n_visiteur):
    """Met à jour un visiteur"""
    data = request.json
    result = visiteur_model.update(
        n_visiteur,
        data.get('nom'),
        data.get('adresse')
    )
    if result['success']:
        return jsonify(result)
    return jsonify(result), 400

@visiteur_bp.route('/api/visiteurs/<n_visiteur>', methods=['DELETE'])
def delete_visiteur(n_visiteur):
    """Supprime un visiteur"""
    result = visiteur_model.delete(n_visiteur)
    if result['success']:
        return jsonify(result)
    return jsonify(result), 400

@visiteur_bp.route('/api/visiteurs/search', methods=['GET'])
def search_visiteurs():
    """Recherche des visiteurs par numéro ou nom"""
    critere = request.args.get('critere')
    valeur = request.args.get('valeur')
    
    if not critere or not valeur:
        return jsonify({'error': 'Critère et valeur requis'}), 400
    
    resultats = visiteur_model.search(critere, valeur)
    return jsonify(resultats)