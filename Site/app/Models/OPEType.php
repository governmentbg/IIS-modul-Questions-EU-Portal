<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $OP_Pr_E_id
 * @property string     $OP_Pr_E_name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class OPEType extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'OP_EType';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'OP_Pr_E_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'OP_Pr_E_id', 'OP_Pr_E_name', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'OP_Pr_E_id' => 'int', 'OP_Pr_E_name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_e_type()
    {
        return $this->hasMany(OPProc::class, 'OP_Pr_E_id');
    }
}
